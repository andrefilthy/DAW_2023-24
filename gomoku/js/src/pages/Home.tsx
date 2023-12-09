import * as React from "react"
//import {useState} from 'react'
import TopBar, {Link} from '../components/TopBar'


const links :Link [] = localStorage.getItem("accessToken") == null ? 
    [
        {name:"Register", path:"/register"},
        {name:"Login", path: "/login"},
    ] : 
    [ 
        {name:"Play", path:"/game"},
        {name:"Logout", path:"/"}
    ] 


export default function Home():React.ReactElement
{        return(
        <div>
            <h1>Home</h1>
            {<TopBar links = {links} />}
        </div>
    )
}