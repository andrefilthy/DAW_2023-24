import { CellPosition } from "./components/Cell"

const rootPath = "api"

export async function fetchInfo(){
    return fetch(`${rootPath}/info`)
}

export async function fetchHome(token : string | undefined) {
    (token)
    if(token === undefined){
        return fetch(`${rootPath}/`)
    }
    return fetch(`${rootPath}/`, {headers : {"Authorization" : `Bearer ${token}`}})
}

export async function RegisterUser(username : string, password : string) {
    return fetchAuthentication("register", username, password)
}

export async function LoginUser(username : string, password : string) {
    return fetchAuthentication("login", username, password)
}

export async function getLeaderboard(page : number){
    let size = 10
    return fetch(`/${rootPath}/stats?size=${size}&offset=${size*(page-1)}`)
}

export async function getGameByUser(token : string) {
    return fetch(`/${rootPath}/game`, {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        }
    })
}

export async function getGame(id: string, token: string) {
    return fetch(`/${rootPath}/game/${id}`, {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        }
    })
}

async function fetchAuthentication(type: string, username : string, password : string) {
    return fetch(`/${rootPath}/${type}`, { 
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
    return fetch(`/${rootPath}/game`, {
        method : "POST",
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : `Bearer ${token}`
        },
        body: JSON.stringify({
            ruleSet : {
                boardSize : 15,
                placingTime : 125
            }
        })
    })
}

export async function postPlay(id: string, play: CellPosition, token: string){
    return fetch(`/${rootPath}/game/${id}/set`, {
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