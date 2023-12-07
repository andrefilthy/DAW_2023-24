import * as React from 'react'
import {
    useState,
    createContext,
    useContext,
} from 'react'
import { CookiesProvider } from 'react-cookie'
import useCookies from 'react-cookie/cjs/useCookies'

type ContextType = {
    token: string ,
    setToken: (t : string) => void,
    username : string,
    setUsername: (u : string) => void,
    cookie: any,
    setCookie: (name: string, c : any) => void,
    removeCookie: (name: string) => void
}

const LoggedInContext = createContext<ContextType>({
    token: undefined,
    setToken: () => {},
    username: undefined,
    setUsername: () => {},
    cookie: undefined,
    setCookie: () => {},
    removeCookie: () => {}
})

export function AuthContainer({ children }: {children: React.ReactNode }) {
    const [cookies, setCookie, removeCookie] = useCookies(['access_token', 'username'])
    const [token, setToken] = useState(cookies["access_token"])
    const [username, setUsername] = useState(cookies["username"])


    return(
        <LoggedInContext.Provider value = {{token : token, setToken : setToken, username : username, setUsername : setUsername, cookie : cookies, setCookie : setCookie, removeCookie : removeCookie}}>
            <CookiesProvider>
                {children}
            </CookiesProvider> 
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

export function useAuthCookie(){
    return useContext(LoggedInContext).cookie
}

export function useSetAuthCookie(){
    return useContext(LoggedInContext).setCookie
}

export function useRemoveCookie(){
    return useContext(LoggedInContext).removeCookie
}