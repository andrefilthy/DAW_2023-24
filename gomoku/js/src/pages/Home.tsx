import * as React from "react"
import {useState} from 'react'
import TopBar from '../components/TopBar'

/*
const links : Link [] = 
[
    {name:"register", path:"/register"},
    {name:"login", path: "/login"},
    //Se user estiver logado, então aparecem links diferentes.
]
*/

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

export default function Home():React.ReactElement
{    
    const [props] = useState<HomeProps | null>(null)
    return(
        <div>
            <h1>Home</h1>
            {<TopBar home = {props.self} links = {props.links} />}
        </div>
    )
}