import TweetHeader from "./TweetHeader.jsx";
import TweetFeed from "./TweetFeed.jsx";
import {useState} from "react";
import TweetEditor from "./TweetEditor.jsx";
import SearchResultView from "./SearchResultView.jsx";
import UserProfile from "./UserProfile.jsx";

function TweetIgelFrontend(){
    const [auth, setAuth] = useState({username:null, password: null, loggedIn: false});
    const [username, setUsername] = useState(undefined);
    const [view, setView] = useState("loggedOut");
    const [result, setResult] = useState([])
    const [viewingUsername, setViewingUsername] = useState()

    return <>
        <header>
            <TweetHeader auth={auth} setAuth={setAuth}
                         username={username} setUsername={setUsername}
                         setView={setView}
                         setResult={setResult}
                         setViewingUsername={setViewingUsername} />
        </header>
        <hr />
        <main>
            {view === "loggedOut"
                ?<div>
                    <h3>Welcome to TweetIgel!</h3>
                    <h5><i>Please log in or register to continue.</i></h5>
                    <img src="/tweetigel_logo.png" width="100" alt="Logo"/>
                </div>
                : view === "loggedIn" ?
                    <div>
                        <h3>
                            <img src="/tweetigel_logo.png" width="100" alt="Logo"/>
                            Hi, {username}!
                        </h3>
                        <TweetEditor></TweetEditor>
                        <h4>What's new</h4>
                        <TweetFeed viewingUsername={null} setViewingUsername={setViewingUsername} setView={setView}></TweetFeed>
                    </div>
                    : view === "search" ?
                        <div>
                            <SearchResultView result={result} setResult={setResult} setView={setView} auth={auth} />
                        </div>
                        : view === "profile" ?
                            <div>
                                <UserProfile viewingUsername={viewingUsername} setViewingUsername={setViewingUsername} setView={setView}/>
                            </div>
                            :<>
                            </>
            }
        </main>
        <hr />
        <footer>
            <i><small>TweetIgel - SoSe2025 - Coursework for "Web Technology Project" By R.Gan</small></i>
        </footer>

    </>
}

export default TweetIgelFrontend;