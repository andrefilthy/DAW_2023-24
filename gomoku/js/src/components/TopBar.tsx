import * as React from 'react'
import {Link, useNavigate} from 'react-router-dom'
import {LinkRelation} from '../pages/Home'

type TopBarProps = {
    home : LinkRelation
    links: LinkRelation[]
}

export function TopBar({home, links}: TopBarProps): React.ReactElement {
    const navigate = useNavigate()
    const token = localStorage.getItem("accessToken")
   
    return (
        <ul>
            <button onClick={() => {navigate(home.href)}}>{home.rel}</button>
            { links.map( (link) => <button onClick={() => {navigate(link.href)}}>{link.rel}</button>) }
            {token && <button onClick={logout}>logout</button>}
        </ul>
    );

    function logout(){
        localStorage.removeItem("accessToken")
        localStorage.removeItem("username")
        navigate("/")
    }
}