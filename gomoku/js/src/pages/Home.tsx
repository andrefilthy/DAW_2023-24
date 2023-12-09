import * as React from "react"
import TopBar, {Link} from '../components/TopBar'
import { useState, useEffect } from "react"

export default function Home():React.ReactElement
{
    const [links, setLinks] = useState<Link[]>([])
    useEffect(() => {
        console.log("HOME")
        if(localStorage.getItem("accessToken") == null){
            setLinks([
                {name:"Register", path:"/register"},
                {name:"Login", path: "/login"},
            ])
        } else {
            setLinks([
                {name:"Play", path:"/game"},
                {name:"Logout", path:"/"}
            ] )
        }
    }, [localStorage.getItem("accessToken")])

    return(
        <div>
            <h1>Home</h1>
            {<TopBar links = {links} />}
        </div>
    )
}