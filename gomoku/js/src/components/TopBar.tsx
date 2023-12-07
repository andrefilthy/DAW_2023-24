import * as React from 'react'
import {Link, useNavigate} from 'react-router-dom'
import {LinkRelation} from '../pages/Home'
import { useAuthCookie, useRemoveCookie } from '../authContainer'

type TopBarProps = {
    home : LinkRelation
    links: LinkRelation[]
}

export function TopBar({home, links}: TopBarProps): React.ReactElement {
    const navigate = useNavigate()
    const cookies = useAuthCookie()
    const removeCookie = useRemoveCookie()
    const token = cookies["access_token"]
   
    return (
        <ul>
            <button onClick={() => {navigate(home.href)}}>{home.rel}</button>
            { links.map( (link) => <button onClick={() => {navigate(link.href)}}>{link.rel}</button>) }
            {token && <button onClick={logout}>logout</button>}
        </ul>
    );

    function logout(){
        removeCookie("access_token")
        removeCookie("username")
        navigate("/")
    }
}