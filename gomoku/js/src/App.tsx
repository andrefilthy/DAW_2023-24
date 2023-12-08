import * as React from 'react'
import {
    createBrowserRouter, RouterProvider,
} from 'react-router-dom'
import Home from './pages/Home'
import Register from './pages/Register'

const router = createBrowserRouter([
    {
        "path": "/",
        "element": <Home />
    },
    {
        "path": "/register",
        "element": <Register />
    }
])

export function App() {
    return (
        <RouterProvider router={router} />
    )
}