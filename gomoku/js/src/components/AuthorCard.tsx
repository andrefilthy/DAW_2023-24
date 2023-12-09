import * as React from 'react'

export type Author = {
    name : string,
    email : string,
    studentNumber : number
}

export function AuthorCard({name, email, studentNumber} : Author): React.ReactElement{
    return (
        <div >
            <h3>{name}</h3>
            <p>email : {email}</p>
            <p>student Number : {studentNumber}</p>
        </div>
    ) 
}