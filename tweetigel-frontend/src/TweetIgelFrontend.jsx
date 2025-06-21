import TweetHeader from "./TweetHeader.jsx";
import {useState} from "react";
import Editor from "./Editor.jsx";
import SearchResultView from "./SearchResultView.jsx";
import UserProfile from "./UserProfile.jsx";
import TweetFeedWithPagination from "./TweetFeedWithPagination..jsx";
import HashTagFeed from "./HashTagFeed.jsx";

function TweetIgelFrontend(){
    const [auth, setAuth] = useState({username:null, loggedIn: false});
    const [username, setUsername] = useState(undefined);
    const [view, setView] = useState("loggedOut");
    const [searchTerm, setSearchTerm] = useState(undefined);
    const [result, setResult] = useState([])
    const [viewingUsername, setViewingUsername] = useState()
    const [hashtag, setHashtag] = useState(undefined);

    return <>
            <header>
                <TweetHeader auth={auth} setAuth={setAuth}
                             username={username} setUsername={setUsername}
                             setView={setView} setSearchTerm={setSearchTerm}
                             setResult={setResult} setViewingUsername={setViewingUsername}/>
            </header>
            <hr/>
            <main>
                {view === "loggedOut"
                    ? <div>
                        <h3>Welcome to <p className="pico-color-pumpkin-700">TweetIgel</p></h3>
                        <h5><i>Please log in or register to continue.</i></h5>
                        <img src="/tweetigel_logo.png" width="100" alt="Logo"/>
                    </div>
                    : view === "loggedIn" ?
                        <div>
                            <h3>
                                <img src="/tweetigel_logo.png" width="100" alt="Logo"/>
                                Hi, {username}!
                            </h3>
                            <Editor endpoint={`/post`} type={`Post`} onSend={null}/>
                            <h4>What's new</h4>
                            <TweetFeedWithPagination username={username} viewingUsername={null} setViewingUsername={setViewingUsername} setView={setView} setHashtag={setHashtag}/>
                        </div>
                        : view === "search-user" || view ==="search-post"?
                            <div>
                                <SearchResultView result={result} setResult={setResult} view={view} setView={setView} setViewingUsername={setViewingUsername} username={username} setHashtag={setHashtag} searchTerm={searchTerm}/>
                            </div>
                            : view === "profile" ?
                                <div>
                                    <UserProfile username={username} viewingUsername={viewingUsername}
                                                 setViewingUsername={setViewingUsername} setView={setView} setHashtag={setHashtag}/>
                                </div>
                                : view === "hashtag" ?
                                    <HashTagFeed hashtag={hashtag} setHashtag={setHashtag} setView={setView} username={username}
                                                 viewingUsername={null} setViewingUsername={setViewingUsername}/>
                                    : <>
                                    </>
                }
            </main>
            <hr/>
            <footer>
                <i><small>TweetIgel - SoSe2025 - Coursework for "Web Technology Project" By R.Gan</small></i>
            </footer>
    </>
}

export default TweetIgelFrontend;