import * as React from "react"
import {
    useState,
    createContext,
    useContext
} from 'react'

type AuthnContextType = {
    loggedIn: boolean,
    setLoggedIn: (v: boolean) => void
}

