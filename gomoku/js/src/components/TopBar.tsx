import * as React from "react"
import { useNavigate } from "react-router-dom"

export type Link = {
    name:string,
    path:string
}
type Props = {
    links : Link []
}

export default function TopBar ({links}:Props) : React.ReactElement {
    const navigate = useNavigate()

    if(localStorage.getItem("accessToken") != null) {
        links.splice(links.indexOf({name:"Logout", path:"/"}, 1)) 
    }

    function clearTokenAndNavigate(path:string){
        localStorage.removeItem("accessToken")
        navigate(path)
    }

    return(
        <ul>  
            { links.map(link => (<button key={link.name} onClick={() => navigate(link.path)}>{link.name}</button>))}
            {localStorage.getItem("accessToken") && <button onClick={() => clearTokenAndNavigate("/")}>Logout</button> }
        </ul>
    )
}