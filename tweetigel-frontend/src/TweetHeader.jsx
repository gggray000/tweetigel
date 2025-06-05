import {useContext, useRef, useState} from "react";
import {API} from "./Context.js";
import {basic, contentTypeJson} from "./RequestHeaders.js";

function TweetHeader({auth, setAuth, username, setUsername, setView, setResult, setViewingUsername}){
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
                if(!response.ok) alert("Unable to log out: " + response.statusText);
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

    function showProfile(username){
        setViewingUsername(username)
        setView("profile")
    }

    if(!auth.loggedIn){
        return <>
            <nav>
                <ul>
                    <li>
                    <label htmlFor="new-account"><small>
                        <input id="new-account"
                               type="checkbox"
                               defaultValue={registering}
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
                            ? <input className="pico-background-azure-450" type="submit" value="Register"/>
                            : <input className="pico-background-azure-450" type="submit" value="Log in"/>
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
                    <div>
                        <div>
                            <li><label><small>Logged in as {username}</small></label></li>
                            <li><button className="pico-background-azure-50" onClick={logOut}>Log Out</button></li>
                            <li><button className="pico-background-azure-450" onClick={() => showProfile(username)}>Profile</button></li>
                        </div>
                        <div>
                            <li>
                                <form onSubmit={search}>
                                    <fieldset role="group">
                                        <input type="text" name="username" placeholder="Find someone..." ref={searchTerm}/>
                                        <input className="pico-background-azure-450" type="submit" value="Search"/>
                                    </fieldset>
                                </form>
                            </li>
                        </div>
                    </div>
                </ul>
            </nav>
        </>
    }
}

export default TweetHeader