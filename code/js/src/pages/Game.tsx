import * as React from 'react'
import { useEffect, useState } from 'react'
import { Board, BoardProps, BoardType } from '../components/Board'
import { CellPosition } from '../components/Cell'
import { getGame, getGameByUser, postPlay, giveUpGame } from '../ApiCalls'
import { useLocation, useParams } from 'react-router-dom'
import { LinkRelation} from './Home'
import { useNavigate } from 'react-router-dom'
const _ = require('lodash')

const POLL_INTERVAL = 1000

export enum GameState {
    NEXT_PLAYER1 = "NEXT_PLAYER1",
    NEXT_PLAYER2 = "NEXT_PLAYER2",
    PLAYER1_WON = "PLAYER1_WON",
    PLAYER2_WON = "PLAYER2_WON",
    PLAYER1_WAITING = "PLAYER1_WAITING",
    PLAYER2_WAITING = "PLAYER2_WAITING"
}

enum GamePhase {
    PLACING = "PLACING",
    COMPLETED = "COMPLETED"
}


type GameProps = {
    resultInfo : string,
    player1: UserProps,
    player2: UserProps,
    board : BoardType,
    state : GameState,
    phase : GamePhase
}

type UserProps = {
    username : string,
    numberOfWins : number,
    numberOfGames : number
}

type ActionProps = {
    fields : null
    href : string,
    method : string,
    name : string,
    type : string
}

type UserEntity = {
    links : LinkRelation[],
    properties : UserProps,
    rel : string
}


type Props = {
    class : string,
    selfLink : LinkRelation,
    entities : UserEntity[],
    actions : ActionProps[],
    properties : GameProps
}

export function Game() : React.ReactElement {
    const [props, setProps] = useState<Props | null>(null)
    const [polling, setPolling] = useState(true); // Controla o estado do polling
    const username = localStorage.getItem("username") 
    const { state }  = useLocation()
    const token = localStorage.getItem("accessToken")
    const { id } = useParams()
    const navigate = useNavigate()

    const contents = props === null ?
        <div></div> :
        renderGame()

        useEffect(() => {
            if (state != null) {
                setProps(sirenToProps(state));
                return
            }
            const fetchData = async () => {
                ("fetch")
                const res = await getGame(id, token);
                const data = await res.json();
                setProps(sirenToProps(data));
            }
        
            fetchData()

        }, []);

    return(
        <div>
            {contents}
        </div>
    )
    

    function renderGame() : React.ReactElement | null{
        const gamePhase = props?.properties.phase
        if(gamePhase === GamePhase.PLACING && polling){
            pollUntilTurn().then(res => setProps(res))
        }

        const board = setupBoards(props.properties.board)
        
        return (
            <div className='game-container'>   
                {gameInfo()}
                <div className='player-board'>
                    <Board {...board}/>
                </div>
                {gamePhase === GamePhase.PLACING && <button onClick={() => giveUp()}>Desistir</button>}
                <button onClick={() => handleGoToMenu()} >Ir para o menú </button>
            </div>
        )
    }


    function handleGoToMenu() {
        setPolling(false)
        navigate('/')
        window.location.reload()
    }

    function setupBoards(board : BoardType) : BoardProps {
        const gamePhase = props.properties.phase
        if(gamePhase === GamePhase.PLACING){
            return {
                board: board,
                play: play
            }
        }
        else {
            return  {board: board}
        }
    }

    async function play(position: CellPosition) {
        const res = await postPlay(id, position, token)
        const body = await res.json()
        if(body.error != undefined) {
            console.log(body.error)
        }
        if(body.error == null) setProps(sirenToProps(body))
    }

    async function giveUp(){
        const res = await giveUpGame(token, id)
        const body = await res.json()
        if(body.error != undefined) {
            console.log(body.error)
        }
        if(body.error == null) setProps(sirenToProps(body))   
    }

    function gameInfo() : React.ReactElement{
        let content = <></>
        const gamePhase = props.properties.phase
        const gameState = props.properties.state
        const className = 'game-info'

        if(gamePhase === GamePhase.PLACING){
            content = isPlayerTurn() ? 
                <p className={className}>Your turn to play</p> :
                <p className={className}>Opponent&#39;s turn to play</p>
        }
        else{
            if(isPlayer1()){
                if(gameState === GameState.PLAYER1_WON) content = <p className={className}>YOU WON</p> 
                else if(gameState === GameState.PLAYER2_WON) content = <p className={className}>YOU LOST</p>
            }
            else if(isPlayer2()){
                if(gameState === GameState.PLAYER2_WON) content = <p className={className}>YOU WON</p>
                else if(gameState === GameState.PLAYER1_WON) content = <p className={className}>YOU LOST</p>
            }
        }

        return(
            <div>
                {content}
            </div>
        )
    }

    function isPlayerTurn(){
        if(props.properties.phase === GamePhase.PLACING){
            if(props.properties.player1.username === username && props.properties.state === GameState.NEXT_PLAYER1) return true
            if(props.properties.player2.username === username && props.properties.state === GameState.NEXT_PLAYER2) return true
            return false
        }
        return true
    }

    function isPlayer1() : boolean {
        return username === props.properties.player1.username
    }

    function isPlayer2() : boolean {
        return username === props.properties.player2.username
    }

    async function pollUntilTurn() {
        let data = await getGame(id, token).then(res => res.json())
        while(_.isEqual(data.properties.board, props.properties.board) && polling){
            await sleep(POLL_INTERVAL)
            data = await getGame(id, token).then(res => res.json())
        }
        return sirenToProps(data)
    }
}

function sleep(millis:number) {
    return new Promise(r =>  setTimeout(r, millis))
}

export async function pollGame(token:string){
    let data = await getGameByUser(token).then(res => res.json())
    while (data.error == 'GameNotFound'){
        await sleep(POLL_INTERVAL)
        data = await getGameByUser(token).then(res => res.json())    
         
    }
    return data

}


function sirenToProps(sirenJson: { class: any[]; links: any[]; entities: any[]; actions: any; properties: { resultInfo: any; player1: any; player2: any; board: any; state: any; phase: any } }) : Props{
    const clazz = sirenJson.class[0]
    const selfLink = sirenJson.links[0]
    const user1 = sirenJson.entities[0]
    const user2 = sirenJson.entities[1]
    const entities = [user1, user2]
    const actions = sirenJson.actions
    const gameProps = {
        resultInfo : sirenJson.properties.resultInfo,
        player1 : sirenJson.properties.player1,
        player2 : sirenJson.properties.player2,
        board : sirenJson.properties.board,
        state : sirenJson.properties.state,
        phase : sirenJson.properties.phase
    }
    return {
        class : clazz,
        selfLink : selfLink,
        entities : entities,
        actions : actions,
        properties : gameProps
    }
}