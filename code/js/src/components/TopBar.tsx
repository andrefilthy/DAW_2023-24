import * as React from 'react'
import {useNavigate} from 'react-router-dom'
import {LinkRelation} from '../pages/Home'

type TopBarProps = {
    home : LinkRelation
    links: LinkRelation[]
}

export function TopBar({home, links}: TopBarProps): React.ReactElement {
    const navigate = useNavigate()
   
    return (
        <ul>
            { links.map( (link) => <button onClick={() => {navigate(link.href)}} key={link.href}>{link.rel}</button>) }
        </ul>
    );
}