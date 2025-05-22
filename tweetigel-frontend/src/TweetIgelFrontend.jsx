import TweetIgelHeader from "./TweetIgelHeader.jsx";
import TweetIgelFeed from "./TweetIgelFeed.jsx";
import {useState} from "react";
import TweetEditor from "./TweetEditor.jsx";

function TweetIgelFrontend(){
    const [auth, setAuth] = useState({username:null, password: null, loggedIn: false});
    const [username, setUsername] = useState(undefined);
    const [view, setView] = useState("loggedOut");



    return <>
        <header>
            <TweetIgelHeader auth={auth} setAuth={setAuth} username={username} setUsername={setUsername} setView={setView}/>
        </header>
        <main>
            {view === "loggedOut"
                ?<div>
                    <h3>Welcome to TweetIgel!</h3>
                    <h4>Please log in or register to continue.</h4>
                    <img src="/tweetigel_logo.png" width="100" alt="Logo"/>
                </div>
                :<div>
                    <h3>Hi, {username}!</h3>
                    <TweetEditor></TweetEditor>
                    <h4>What's new</h4>
                    <TweetIgelFeed></TweetIgelFeed>
                </div>
            }
        </main>
        <footer>TweetIgel - SoSe2025 - Web Technology Project By Ruilin Gan</footer>

    </>
}

export default TweetIgelFrontend;