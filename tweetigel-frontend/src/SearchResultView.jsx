import FollowButton from "./FollowButton.jsx";

function SearchResultView({result, setResult, setView, setViewingUsername}){

    function showProfile(username){
        setView("profile")
        setViewingUsername(username)
    }

    function goBack(){
        setView("loggedIn")
    }

    function followButtonOp(username){
        const index = result.findIndex(user => user.username === username);
        const oldStatus = result[index].followed;
        const newResult = result.slice();
        newResult[index].followed = !oldStatus
        setResult(newResult)
    }

    return <>
        <div className="search-header">
            {result.length === 0? <h3>No user found.</h3> :<h3>User Search Result</h3>}
            <br/>
            <button className="pico-background-azure-450" onClick={goBack}>Go Back to Feed</button>
        </div>
        <br/>
        {result.length === 0? <></>
            : <table className="search-table">
                <thead>
                <tr>
                    <th scope="col"><h5>Username</h5></th>
                    <th scope="col"><h5>Followed</h5></th>
                    <th scope="col"><h5>Action</h5></th>
                </tr>
                </thead>
                <tbody>
                {result.map(user => (
                    <tr key={user.username}>
                        <th scope="row" >
                            <i>{user.username}</i>
                            <a href="#" onClick={() => showProfile(user.username)} > <sub><i>Profile</i></sub></a>
                        </th>
                        <td>{user.followed?"✅":"❌"}</td>
                        <td>{user.followed?
                            <FollowButton username={user.username} followed={true} func={followButtonOp} />
                            : <FollowButton username={user.username} followed={false} func={followButtonOp}/>}
                        </td>
                    </tr>
                ))
                }
                </tbody>
        </table>
        }
    </>
}

export default SearchResultView