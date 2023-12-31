import * as React from 'react'
import {useState, useEffect} from 'react'
import { TopBar } from '../components/TopBar'
import { fetchHome, getGameByUser, startGame } from '../ApiCalls'
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
        &nbsp;<button onClick={() => searchGame()}>Play</button>
        </div>
        //&nbsp;<button onClick={() => removeFromWaitingList}>Leave Waiting List</button>
        

    useEffect(() => {
        const fetchData = async () => {
            const data = await fetchHome(token)
            const sirenobj = await data.json()
            const arr = sirenobj.links as LinkRelation[]
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
            const info1 = await getGameByUser(token)
            if(info1.status === 404)
                alert("Searching opponent... Click OK to procceed.")
            pollGame(token).then(game => {
                navigate(`/game/${game.properties.gameID}`, { state : game})
                return
            })
        }else if(info.status == 403){
            const data = await info.json()
            alert(data.message)
            return
        }
    }

  /* async function removeFromWaitingList(){
        const info = await deleteFromWaitingList(username)
        if(info.status == 200){
            alert("Foi removido da lista de espera.")
            navigate(`/`)
            return
        }
        else {
            alert("Ocorreu um erro ao removê-lo da lista de espera")
            navigate('/')
            return
        }
    } */

    function logout(){
        localStorage.removeItem("accessToken")
        localStorage.removeItem("username")
        navigate("/")
    }
}

