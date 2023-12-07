import * as React from 'react'
import {useState, useEffect} from 'react'
import {useNavigate} from 'react-router-dom'
import { TopBar } from '../components/TopBar'
import { fetchHome } from '../ApiCalls'
import { Authenticate } from '../components/Authenticate'
import { startGame, getGameByUser } from '../ApiCalls'
import { pollGame } from './Game'
import useCookies from 'react-cookie/cjs/useCookies'
import { useAuthCookie } from '../authContainer'
import { withCookies, Cookies } from 'react-cookie';


export type LinkRelation = {
    rel : string[],
    href : string
}

export type User = {
    username : string,
    password : string
}


type HomeProps = {
    user : User | null,
    self : LinkRelation
    links : LinkRelation[]
    action : null
}

export function Home(): React.ReactElement {
    const [props, setProps] = useState<HomeProps | null>(null)
    const cookies = useAuthCookie()
    const navigate = useNavigate()

    const token = cookies["access_token"]

    console.log(token)
    let contents = token === undefined ? 
        <Authenticate /> :
        <button onClick={() => searchGame()}>Play</button>

    useEffect(() => {
        const fetchData = async () => {
            const data = await fetchHome(token)
            const sirenobj = await data.json()
            var arr = sirenobj.links as LinkRelation[]
            const self = arr.find(e => e.rel.includes("self"))

            setProps({
                user : sirenobj.user,
                self : arr.splice(arr.indexOf(self))[0],
                links : arr,
                action : null
            })
          }

        fetchData()
    },[token])

    return (
        <div>
            {props && <div>
                <h1>Home</h1>
                {<TopBar home={props.self} links={props.links} />}
                {contents}
            </div>}
        </div>
    )

    async function searchGame(){
        const info = await startGame(token)
        if(info.status == 201){
            const data = await info.json()
            navigate(`/game/${data.properties.gameID}`, { state : data})
            return
        }else if(info.status == 200){
            pollGame(token).then(game => {
                navigate(`/game/${game.properties.gameID}`, { state : game})
            })
        }else if(info.status == 403){
            const data = await info.json()
            alert(data.message)
        }
        
    }
}