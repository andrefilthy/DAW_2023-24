import * as React from "react"
import TopBar, { Link } from '../components/TopBar'

const links :Link[] = 
[
    {name:"register", path:"/register"},
    {name:"login", path: "/login"},
    //Se user estiver logado, então aparecem links diferentes.
]



export default function Home():React.ReactElement
{
    return(
        <div>
            <h1>Home</h1>
            <TopBar links={links} />
        </div>
    )
}

