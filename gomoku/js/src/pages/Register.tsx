import * as React from 'react'
import { useNavigate } from 'react-router-dom'
import { RegisterUser } from '../ApiCalls'
import {useSetAuthCookie, useSetToken, useSetUsername, useToken} from '../authContainer'
import { Auth } from '../components/Auth'


export function Register() {
    const setUsername = useSetUsername()
    const setToken = useSetToken()
    const navigate = useNavigate()
    const setCookie = useSetAuthCookie()

    function onClick(){
        const username = document.getElementById("username") as HTMLInputElement
        const password = document.getElementById("password") as HTMLInputElement
        const sendRequest = async () => {
            const resp = await RegisterUser(username.value, password.value)
            const body = await resp.json()
            if(resp.status === 201){
                setCookie( "access_token", body.access_token)
                setCookie("username", body.username)
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

