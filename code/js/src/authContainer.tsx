import * as React from 'react'
import {
    useState,
    createContext,
    useContext,
} from 'react'

type ContextType = {
    token: string ,
    setToken: (t : string) => void,
    username : string,
    setUsername: (u : string) => void
}

const LoggedInContext = createContext<ContextType>({
    token: undefined,
    setToken: () => {},
    username: undefined,
    setUsername: () => {}
})

export function AuthContainer({ children }: {children: React.ReactNode }) {
    const [token, setToken] = useState(localStorage.getItem("access_token"))
    const [username, setUsername] = useState(localStorage.getItem("username"))
    

    return(
        <LoggedInContext.Provider value = {{token : token, setToken : setToken, username : username, setUsername : setUsername}}>
           {children}
        </LoggedInContext.Provider>
    )
}

export function useToken(){
    return useContext(LoggedInContext).token
}

export function useSetToken(){
    return useContext(LoggedInContext).setToken
}

export function useUsername() {
    return useContext(LoggedInContext).username
}

export function useSetUsername() {
    return useContext(LoggedInContext).setUsername
}