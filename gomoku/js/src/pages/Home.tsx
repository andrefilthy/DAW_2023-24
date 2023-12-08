import * as React from "react"

type Link = {
    name:string,
    path:string
}

const links :Link[] = 
[
    {name:"register", path:"./register"},
    {name:"login", path: "./login"},
    //Se user estiver logado, então aparecem links diferentes.
]



export default function Home():React.ReactElement
{
    return(
        <div>
            <h1>Olá</h1>
        </div>
    )
}

