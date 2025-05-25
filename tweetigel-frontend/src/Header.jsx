import {useContext, useRef, useState} from "react";
import {API} from "./Context.js";
import {basic, contentTypeJson} from "./RequestHeaders.js";

function Header({auth, setAuth, username, setUsername, setView, setResult}){
    const api = useContext(API)
    const [registering, setRegistering] = useState(false)
    const name = useRef(undefined)
    const password = useRef(undefined)
    const searchTerm = useRef(undefined)

    function logOut(){
        fetch(api+"/user/logout",{
            method:"POST",
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
        const registerAuth = {username: name.current.value, password: password.current.value}
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

    function search(){
        event.preventDefault();
        fetch(api+"/user/search?term=" + searchTerm.current.value,
            {method:"GET",
                 credentials: 'include'
            }).then(response => {
            if(!response.ok) {
                alert("Unable to Search");
                return []
            }else{
                return response.json()
            }
        }).then(result => {
            setResult(result)
            setView("search")
        })
        searchTerm.current.value = ""
    }

    if(!auth.loggedIn){
        return <>
            <nav>
                <ul>
                    <li>
                    <label htmlFor="new-account"><small>
                        <input id="new-account" type="checkbox" defaultValue={registering}
                               onChange={e => setRegistering(e.target.checked)}/>
                        Create an new account</small>
                    </label>
                    </li>
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
                    <li><label><small>Logged in as {username}</small></label></li>
                </ul>
                <ul>
                    <li><button className="pico-background-zinc-500" onClick={logOut}>Log Out</button></li>
                </ul>
                <ul>
                    <li>
                        <form role = "search" onSubmit={search}>
                            <input name="search" type="username" placeholder="Username" ref={searchTerm}/>
                            <input type="submit" value="Search"/>
                        </form>
                    </li>
                </ul>
            </nav>
        </>
    }

}

export default Header