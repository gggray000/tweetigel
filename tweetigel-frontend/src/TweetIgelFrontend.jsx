import Header from "./Header.jsx";
import TweetFeed from "./TweetFeed.jsx";
import {useState} from "react";
import TweetEditor from "./TweetEditor.jsx";
import SearchResultView from "./SearchResultView.jsx";

function TweetIgelFrontend(){
    const [auth, setAuth] = useState({username:null, password: null, loggedIn: false});
    const [username, setUsername] = useState(undefined);
    const [view, setView] = useState("loggedOut");
    const [result, setResult] = useState([])

    return <>
        <header>
            <Header auth={auth} setAuth={setAuth}
                    username={username} setUsername={setUsername}
                    setView={setView}
                    setResult={setResult} />
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
                        <TweetFeed></TweetFeed>
                    </div>
                    : view === "search" ?
                        <div>
                            <SearchResultView result={result} setResult={setResult} setView={setView} auth={auth} />
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