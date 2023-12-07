import * as React from 'react'
import { useEffect, useState } from 'react'
import { Board, BoardProps, BoardType } from '../components/Board'
import { useLocation, useParams } from 'react-router-dom'
import { User, LinkRelation} from './Home'
import { useAuthCookie, useToken, useUsername } from '../authContainer'
import { getGame, postReady, getGameByUser, postPlay, PLACING_TIME } from '../ApiCalls'
import { CellPosition } from '../components/Cell'
import { PlacementButton } from '../components/PlacementButton'
import { Timer } from '../components/Timer'
import _ from 'lodash'

//time that polling function will wait until next request (1 second)
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

type UserEntity = {
    links : LinkRelation[],
    properties : UserProps,
    rel : string
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

type GameProps = {
    resultInfo : string,
    player1: UserProps,
    player2: UserProps,
    board : BoardType,
    state : GameState,
    phase : GamePhase
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
    const cookies = useAuthCookie()
    const token = cookies["access_token"]
    const username = cookies["username"]
    const { state }  = useLocation()
    const { id } = useParams()
    console.log(username)
    console.log(token)

    let contents = props === null ?
        <div></div> :
        renderGame()

    useEffect(() => {
        
        if(state != null) {
            setProps(sirenToProps(state))
            return 
        }
        const fetchData = async () => {
            ("fetch")
            const res = await getGame(id, token)
            const data = await res.json()
            setProps(sirenToProps(data))
        }
        fetchData()
    }, [])
    
    return(
    <div>
        {contents}
    </div>
    )

    function renderGame() : React.ReactElement{
        
        
    }

    function gameInfo() : React.ReactElement{
        let content = <></>
        const gamePhase = props.properties.phase
        const gameState = props.properties.state
        let className = 'game-info'

        
        if(gamePhase === GamePhase.PLACING){
            content = isPlayerTurn() ? 
                <p className={className}>Your turn to place</p> :
                <p className={className}>Opponent's turn to place</p>
        }
        else{
            if(isPlayer1()){
                if(gameState === GameState.PLAYER1_WON){
                    content = <p className={className}>YOU WON</p>
                }else if(gameState === GameState.PLAYER2_WON){
                    content = <p className={className}>YOU LOST</p>
                }
            }else if(isPlayer2()){
                if(gameState === GameState.PLAYER2_WON){
                    content = <p className={className}>YOU WON</p>
                }else if(gameState === GameState.PLAYER1_WON){
                    content = <p className={className}>YOU LOST</p>
                }
            }
        }

        return(
            <div>
                {content}
            </div>
        )
    }

    function isPlayer1() : boolean {
        return username === props.properties.player1.username
    }

    function isPlayer2() : boolean {
        return username === props.properties.player2.username
    }

    

    
    async function place(position: CellPosition) {
        const res = await postPlay(id, position, token)
        const body = await res.json()
        setProps(sirenToProps(body))
    }

    function isPlayerTurn(){
        if(props.properties.phase === GamePhase.PLACING){
            if(props.properties.player1.username === username && props.properties.state === GameState.NEXT_PLAYER1) return true
            if(props.properties.player2.username === username && props.properties.state === GameState.NEXT_PLAYER2) return true
            return false
        }
        return true
    }

    async function readyUp(){
            const res = await postReady(id, token)
            const body = await res.json()
            let data = sirenToProps(body)
            if(data.properties.resultInfo === "WaitingForOtherPlayer"){
                pollGamePlacing(token).then(game => {
                    setProps(sirenToProps(game))
                })
            }else{
                setProps(data)
            }
            
    }


    async function pollUntilTurn() {
        let data = await getGame(id, token).then(res => res.json())
        while(_.isEqual(data.properties.board1, props.properties.board1) && _.isEqual(data.properties.board2, props.properties.board2)){
            await sleep(POLL_INTERVAL)
            data = await getGame(id, token).then(res => res.json())
        }
        return sirenToProps(data)
    }
}

function sirenToProps(sirenJson) : Props{
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

function sleep(millis) {
    return new Promise(r =>  setTimeout(r, millis))
}

export async function pollGame(token){
    let data = await getGameByUser(token).then(res => res.json())
    while (data.error == 'GameNotFound'){
        await sleep(POLL_INTERVAL)
        data = await getGameByUser(token).then(res => res.json())    
         
    }
    return data

}

async function pollGamePlacing(token){
    let data = await getGameByUser(token).then(res => res.json())
    while (data.properties.state == GameState.PLAYER1_WAITING || data.properties.state == GameState.PLAYER2_WAITING){
        await sleep(POLL_INTERVAL)
        data = await getGameByUser(token).then(res => res.json())    
         
    }
    return data

}

