import * as React from 'react'

type AuthProps = {
    type : string,
    onClick : () => void
}

export function Auth({type, onClick} : AuthProps) {
    
    return (
        <div>
            <form id="auth" >
                <label>Username</label><br/>
                <input type="text" id="username"/><br/>
                <label>Password</label><br/>
                <input type="password" id="password"/><br/><br/>
                <button type="button" onClick={onClick}>
                    {type}
                </button>
               
            </form> 
        </div>
    )
}