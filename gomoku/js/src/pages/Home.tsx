import * as React from 'react'
import {useState, useEffect} from 'react'
import { TopBar } from '../components/TopBar'
import { fetchHome } from '../ApiCalls'
import { Authenticate } from '../components/Authenticate'
import { useNavigate } from 'react-router-dom'


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
    const navigate = useNavigate()
    const [props, setProps] = useState<HomeProps | null>(null)

    const token = localStorage.getItem("accessToken")

    const contents = token == null ? 
        <Authenticate /> :
        <button onClick={() => {}}>Play</button>

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
                {token && <button onClick={logout}>logout</button>}
                {<TopBar home={props.self} links={props.links} />}
                {contents}
            </div>}
        </div>
    )
    function logout(){
        localStorage.removeItem("accessToken")
        localStorage.removeItem("username")
        navigate("/")
    }
}

