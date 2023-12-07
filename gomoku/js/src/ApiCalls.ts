import { CellPosition } from "./components/Cell"


const basePath = "api"

export const PLACING_TIME = 120

export async function fetchInfo(){
    return fetch(`${basePath}/info`)
}

export async function fetchHome(token : string | undefined) {
    (token)
    if(token === undefined){
        return fetch(`${basePath}/`)
    }
    return fetch(`${basePath}/`, {headers : {"Authorization" : `Bearer ${token}`}})
}

export async function RegisterUser(username : string, password : string) {
    return fetchAuthentication("register", username, password)
}

export async function LoginUser(username : string, password : string) {
    return fetchAuthentication("login", username, password)
}

async function fetchAuthentication(type: string, username : string, password : string) {
    return fetch(`/${basePath}/${type}`, { 
        method:"POST",
        headers : {
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify({
            'username' : username,
            'password' : password
        })
    })
}

export async function startGame(token : string){
    return fetch(`/${basePath}/game`, {
        method : "POST",
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        },
        body: JSON.stringify({
            ruleSet : {
                boardSize : 15,
                placingTime : PLACING_TIME
            }
        })
    })
}

export async function getGameByUser(token : string) {
    return fetch(`/${basePath}/game`, {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        }
    })
}

export async function getGame(id: string, token: string) {
    return fetch(`/${basePath}/game/${id}`, {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        }
    })
}

export async function getLeaderboard(page : number){
    let size = 10
    return fetch(`/${basePath}/stats?size=${size}&offset=${size*(page-1)}`)
}

export async function postReady(id: String, token: string){
    
    
    return fetch(`/${basePath}/game/${id}/set`, {
        method: "POST",
        headers: {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        },
        body: JSON.stringify({
        })

    })
}

export async function postPlay(id: string, play: CellPosition, token: string){
    return fetch(`/${basePath}/game/${id}/play`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            play : play
        })
    })
}
