import {useContext, useRef, useState} from "react";
import {API} from "./Context.js";
import {basic, contentTypeJson} from "./Headers.js";

function TweetIgelHeader({auth, setAuth, username, setUsername, setView}){
    const api = useContext(API)
    const [registering, setRegistering] = useState(false)
    const name = useRef(undefined)
    const password = useRef(undefined)

    function logOut(){
        fetch(api+"/user/logout",{
            method:"POST",
            headers:basic(auth),
            credentials: 'include'
        })
            .then(response => {
                if(!response.ok) alert(response.statusText);
            }).then(() => {
                setAuth({name:null, password:null, loggedIn: false})
                setView("loggedOut")
        })
    }

    function logIn(){
        event.preventDefault();
        const logInAuth = {name: name.current.value, password: password.current.value}
        fetch(api+"/user/login",{
            method:"POST",
            headers:basic(logInAuth),
            credentials: 'include'
        })
            .then(response => {
                if(!response.ok) alert("Invalid Credentials.");
                else return response.json();
            }).then(result => {
            setAuth({name:result.username, loggedIn: true})
            setUsername(result.username)
            setView("loggedIn")
        })
    }

    function register(){
        event.preventDefault();
        const registerAuth = {username:name.current.value, password:password.current.value};
        fetch(api+"/register",
            {method:"POST",
                 headers:contentTypeJson(),
                 body: JSON.stringify(registerAuth)
        }).then(response => {
                if(!response.ok) {
                    alert("Register Failed.");
                }else{
                    alert("Successfully registered!")
                    setRegistering(false)
                }
        })
    }

    if(!auth.loggedIn){
        return <>
            <nav>
                <ul>
                    <li><label>Currently Not Logged in.</label></li>
                </ul>
                <ul>
                    <input id="new-account" type="checkbox" defaultValue={registering}
                           onChange={e => setRegistering(e.target.checked)}/>
                    <label htmlFor="new-account">Create a new account</label>
                </ul>
                <ul>
                    <form onSubmit={registering ? register : logIn}>
                        <fieldset role="group">
                            <input name="username" type="username" placeholder="username" ref={name}/>
                            <input name="password" type="password" placeholder="Password" ref={password}/>
                            {registering
                                ? <input type="submit" value="Register"/>
                                : <input type="submit" value="Log in"/>
                            }
                        </fieldset>
                    </form>
                </ul>
            </nav>
        </>
    } else {
        return <>
            <nav>
                <ul>
                    <li><label>Logged In As {username}</label></li>
                </ul>
                <ul>
                    <li><input type="button" value="Log Out" onClick={logOut}/></li>
                </ul>
                <ul>
                    <li>
                        <form role = "search">
                            <input name="search" type="username" placeholder="Enter username to search user"/>
                            <input type="submit" value="Search"/>
                        </form>
                    </li>
                </ul>
            </nav>
        </>
    }

}

export default TweetIgelHeader