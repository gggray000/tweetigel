import {useContext, useRef, useState} from "react";
import {API} from "./Context.js";
import {basic, contentTypeJson} from "./RequestHeaders.js";

function TweetHeader({auth, setAuth, username, setUsername, setView, setViewingUsername, setSearchTerm}){
    const api = useContext(API)
    const [registering, setRegistering] = useState(false)
    const name = useRef(undefined)
    const password = useRef(undefined)
    const term = useRef(undefined)
    const searchMode = useRef(undefined)

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

    function clearSession(){
       if(confirm("This button is only for users who have been accidentally redirected to this page without logging out."))
        fetch(api+"/user/logout",{
            method:"POST",
            credentials: 'include'
        }).then(
            response => {
                if(!response.ok) alert("Troubleshooting failed, please reboot backend or clear cookies.");
            })
    }

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

    function search(){
        event.preventDefault();
        var viewToBeSet;
        const searchTerm = term.current.value
        if(searchMode.current.value === "User") {
            //endpoint = "/user";
            viewToBeSet = "search-user";
        } else {
            //endpoint = "/post";
            viewToBeSet = "search-post";
        }
        /*fetch(api+ endpoint + "/search?term=" + encodeURIComponent(searchTerm),
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
            setView(viewToBeSet)
            setSearchTerm(searchTerm)
        })*/
        setView(viewToBeSet)
        setSearchTerm(searchTerm)
        term.current.value = ""
    }

    function showProfile(username){
        setViewingUsername(username)
        setView("profile")
    }

    if(!auth.loggedIn){
        return <>
            <nav className="landing-header">
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
                <ul>
                    <button className="pico-background-azure-50" onClick={clearSession}>Can't Log In?</button>
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
                                        <input type="text" name="username" placeholder="Looking for..." ref={term}/>
                                        <select className="user-or-post" aria-label="User/Post" ref={searchMode} required>
                                            <option>User</option>
                                            <option>Post</option>
                                        </select>
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