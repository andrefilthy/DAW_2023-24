import * as React from 'react'
import {useState, useEffect} from 'react'
import {useNavigate} from 'react-router-dom'
import { TopBar } from '../components/TopBar'
import { fetchHome } from '../ApiCalls'
import { Authenticate } from '../components/Authenticate'


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
    const [props, setProps] = useState<HomeProps | null>(null)
    const navigate = useNavigate()

    const token = localStorage.getItem("accessToken")

    console.log(token)
    let contents = token === undefined ? 
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
                {<TopBar home={props.self} links={props.links} />}
                {contents}
            </div>}
        </div>
    )
}