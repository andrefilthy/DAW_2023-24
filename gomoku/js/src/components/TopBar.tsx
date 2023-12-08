import * as React from "react"
import { useNavigate } from "react-router-dom"


export type Link = {
    name:string,
    path:string
}

type Props = {
    links:Link[]
}

export default function TopBar({links}:Props):React.ReactElement
{
    const navigate = useNavigate()
    return(
        <div>
            <ul>
                {
                    links.map(link => 
                        (<button onClick={() => navigate(link.path)}>{link.name}</button>)
                    )
                }
            </ul>
        </div>
    )
}