import * as React from "react"
import TopBar, {Link} from '../components/TopBar'

export default function Home():React.ReactElement
{
    const links : Link[] = (localStorage.getItem("accessToken") == null)?
        ([
            {name:"Register", path:"/register"},
            {name:"Login", path: "/login"},
            ])
        :
            ([
                {name:"Play", path:"/game"},
                {name:"Logout", path:"/"}
            ] )

    return(
        <div>
            <h1>Home</h1>
            {<TopBar links = {links} />}
        </div>
    )
}