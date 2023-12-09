import * as React from 'react'
import {useState, useEffect} from 'react'
import {Link, useNavigate} from 'react-router-dom'

import { TopBar } from '../components/TopBar'
import { getLeaderboard } from '../ApiCalls'
import { PlayerStats } from '../components/PlayerStats'
import '../playerStats.css'


type Action = {
    name : string,
    href : string,
    method : string,
    type : string
}

export type LinkRelation = {
    rel : string[],
    href : string
}

type User = {
    username : string,
    numberOfGames : number,
    numberOfWins : number
}

type StatsProps = {
    users : User[],
    
}



type Props = {
    home : LinkRelation,
    links : LinkRelation[],
    properties : StatsProps,
    actions : Action[]
}

export function Stats(): React.ReactElement {
    const navigate = useNavigate()
    const [props, setProps] = useState<Props | null>(null)
    const [page, setPage] = useState<number>(1)

    useEffect(() => {
        const fetchData = async () => {
            const res = await getLeaderboard(page)
            const sirenobj = await res.json()
            
            var arr = sirenobj.links as LinkRelation[]
            const home = arr.find(e => e.rel.includes("home"))
            
            setProps({
                home : arr.splice(arr.indexOf(home),1)[0],
                links : arr,
                properties : sirenobj.properties as StatsProps,
                actions : sirenobj.actions as Action[]
            })
        }
        fetchData()
    },[page])

    //TODO(Handle out of bounds requests to stats page)
    return (
        <div>
            {props &&   
                <div>
                    {<TopBar home={props.home} links={props.links} />}
                    <div className='stats-board'>
                    {props.properties.users.map(u => 
                    <div key={u.username}>
                        <PlayerStats {...u}/>
                    </div>
                    )}
                    </div>
                    {props.actions.map(action =><button onClick={() => {
                        if(action.name == "nextPage") setPage(page+1)
                        else if (action.name == "prevPage") setPage(page-1)
                        }}>{action.name}</button>)}

                </div>}
            
           
        </div>
    )
}