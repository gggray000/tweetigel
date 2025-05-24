import FollowButton from "./FollowButton.jsx";

function SearchResultView({result, setResult, setView}){

    function goBack(){
        setView("loggedIn")
    }

    if(result.length === 0){
        return <>
            <div className="grid">
                <h3>No user found.</h3>
                <input type="button" value="Go Back to Feed" onClick={goBack}/>
            </div>

        </>
    } else{
        return <>
            <div className="grid">
                <h3>User Search Result</h3>
                <input type="button" value="Go Back to Feed" onClick={goBack}/>
            </div>
            <table>
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
                        <th scope="row"><i>{user.username}</i></th>
                        <td>{user.followed?"✅":"❌"}</td>
                        <td>{user.followed?
                            <FollowButton username={user.username} followed={true} result={result} setResult={setResult} />
                            : <FollowButton username={user.username} followed={false} result={result} setResult={setResult}/>}
                        </td>
                    </tr>
                    ))
                }
                </tbody>
            </table>
        </>
    }
}

export default SearchResultView