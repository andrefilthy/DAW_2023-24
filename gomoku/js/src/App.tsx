import * as React from 'react'
import {
    createBrowserRouter, Link, RouterProvider, useParams,
} from 'react-router-dom'
import {Home} from './pages/Home'
import {Register} from './pages/Register'

import { AuthContainer, useToken} from './authContainer'
import { Login } from './pages/Login'
import { Game } from './pages/Game'
import { Info } from './pages/Info'
import { Stats } from './pages/Stats'


const router = createBrowserRouter([
    {
        "path": "/",
        "element": <Home />
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
    },
    {
        "path": "/register",
        "element": <Register />
    },
])

export function App() {
    return (
        <AuthContainer>
            <RouterProvider router={router} />
        </AuthContainer>
    )
}