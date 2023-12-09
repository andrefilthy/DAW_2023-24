import * as React from 'react'
import {createBrowserRouter, RouterProvider } from 'react-router-dom'
import { AuthContainer, useToken} from './authContainer'
import Home from './pages/Home'
import Register from './pages/Register'
import Login  from './pages/Login'
import { Game } from './pages/Game'
import { Info } from './pages/Info'
import { Stats } from './pages/Stats'

const router = createBrowserRouter([
    {
        "path": "/",
        "element": <Home />
    },
    {
        "path": "/register",
        "element": <Register />
    },
    {
        "path": "/login",
        "element": <Login />
    },
    {
        "path": "/game/:id",
        "element": <Game />
    }, 
    {
        "path": "/stats",
        "element": <Stats />
    },
    {
        "path": "/info",
        "element": <Info />
    }
])

export function App() {
    return (
        <AuthContainer>
            <RouterProvider router={router} />
        </AuthContainer>
    )
}