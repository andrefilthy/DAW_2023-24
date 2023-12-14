import * as React from 'react'
import {useState, useEffect} from 'react'
import { TopBar } from '../components/TopBar'
import { fetchHome, startGame  } from '../ApiCalls'
import { Authenticate } from '../components/Authenticate'
import { useNavigate } from 'react-router-dom'
import { pollGame } from './Game'


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

export default function Home(): React.ReactElement {
    const navigate = useNavigate()
    const [props, setProps] = useState<HomeProps | null>(null)

    const token = localStorage.getItem("accessToken")
    const username = localStorage.getItem("username")

    const contents = token == null ? 
        <Authenticate /> :
        <div>Want to play, {username}?    
        &nbsp;<button onClick={() => searchGame()}>Play</button></div>
        

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
                {token && <button onClick={logout}>Logout</button>}
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

    function logout(){
        localStorage.removeItem("accessToken")
        localStorage.removeItem("username")
        navigate("/")
    }
}

