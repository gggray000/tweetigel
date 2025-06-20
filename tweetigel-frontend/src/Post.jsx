import CommentSection from "./CommentSection.jsx";
import HashTag from "./HashTag.jsx";
import {useContext} from "react";
import {API} from "./Context.js";

function Post({post, username, setView, changed, setChanged, viewingUsername, setViewingUsername, feed, setFeed, setHashtag}){
    const api = useContext(API)

    function like(id){
        event.preventDefault();
        fetch(api+"/post/" + id + "/like", {
            method: 'PUT',
            credentials: 'include'
        }).then(response => {
                if (!response.ok) {
                    alert("Unable to like the post: " + response.statusText)
                } else {
                    setChanged(!changed);
                }
            }
        )
    }

    function unlike(id){
        event.preventDefault();
        fetch(api+"/post/" + id + "/unlike", {
            method: 'PUT',
            credentials: 'include'
        }).then(response => {
                if (!response.ok) {
                    alert("Unable to unlike the post: " + response.statusText)
                } else {
                    setChanged(!changed);
                }
            }
        )
    }

    function deletePost(id){
        event.preventDefault();
        if(confirm("Are you sure to delete this post?")){
            fetch(api+"/post/" + id, {
                method: 'Delete',
                credentials: 'include'
            }).then(response => {
                    if (!response.ok) {
                        alert("Unable to delete the post: " + response.statusText)
                    } else {
                        setChanged(!changed);
                    }
                }
            )
        }
    }

    function showProfile(username){
        setView("profile")
        setViewingUsername(username)
    }

    // Reference: https://regex101.com/codegen?language=javascript
    function locateAndTransformHashtags(content) {
        const regex = /#(\w+)/gm;
        const parts = [];
        let lastIndex = 0;
        let match;

        while ((match = regex.exec(content)) !== null) {
            if (match.index > lastIndex) {
                parts.push(content.slice(lastIndex, match.index));
            }

            parts.push(
                <HashTag key={match.index} content={match[0]} setView={setView} setHashtag={setHashtag}/>
            );
            lastIndex = regex.lastIndex;
        }

        if (lastIndex < content.length) {
            parts.push(content.slice(lastIndex));
        }

        return <>{parts}</>;
    }


    return <>
        <article key={post.id}>
            <header>
                <div className="grid">
                    <a href="#" onClick={() => showProfile(post.author.username)}>
                        <b>{post.author.username}</b>
                    </a>
                    <p><small><i>At {post.timestamp}</i></small></p>
                    {post.likeable === true?
                        <button className="pico-background-pink-450" onClick={() => like(post.id)}>
                            Like ♥ {post.likesCount}
                        </button>
                        :post.likeable===false?
                            <button className="pico-background-zinc-500" onClick={() => unlike(post.id)}>
                                Unlike ♡ {post.likesCount}
                            </button>
                            : <></>
                    }
                    { username === viewingUsername?
                        <button className="pico-background-zinc-500" onClick={() => deletePost(post.id)}>
                            Delete ✖ </button>
                        : <></>
                    }
                </div>
            </header>
            <p>{locateAndTransformHashtags(post.content)}</p>
            <footer>
                <CommentSection commentsCount={post.commentsCount} postId={post.id}
                                onCommentAdded={() => {
                                    setFeed(feed.map(p => p.id === post.id
                                        ? { ...p, commentsCount: p.commentsCount + 1 }
                                        : p))
                                }}
                ></CommentSection>
            </footer>
        </article>
    </>
}

export default Post;