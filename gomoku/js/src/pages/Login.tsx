import * as React from 'react'
import { useNavigate } from 'react-router-dom'
import { LoginUser } from '../ApiCalls'
import { Auth } from '../components/Auth'

export default function Login()  : React.ReactElement {

    const navigate = useNavigate()

    function onClick(){
        const username = document.getElementById("username") as HTMLInputElement
        const password = document.getElementById("password") as HTMLInputElement
        const sendRequest = async () => {
            const resp = await LoginUser(username.value, password.value)
            const body = await resp.json()
            if(resp.status === 200){
                localStorage.setItem("accessToken", body.access_token)
                navigate("/")
            }else{
                alert(body.message)
            }   
        }
        sendRequest()
    }
    return (
        <Auth type= {"Login"} onClick={onClick}/>
    )
}