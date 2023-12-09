import * as React from "react"
import { useNavigate } from "react-router-dom"
import { RegisterUser } from "../ApiCalls"
import { Auth } from "../components/Auth"

export default function Register() : React.ReactElement {
    
    const navigate = useNavigate()
    function onClick(){
        const username = document.getElementById("username") as HTMLInputElement
        const password = document.getElementById("password") as HTMLInputElement
        const sendRequest = async () => {
            const resp = await RegisterUser(username.value, password.value)
            const body = await resp.json()
            if(resp.status === 201){
                localStorage.setItem("accessToken", body.accessToken)
                console.log(localStorage.getItem("accessToken"))
                navigate("/")
            }else{
                alert(body.message)
            }
        }
        sendRequest()
    }
    return (
        <Auth type ={"Register"} onClick={onClick}/>
    )
}