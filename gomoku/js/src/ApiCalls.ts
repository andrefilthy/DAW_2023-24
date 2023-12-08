const rootPath = "api"

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