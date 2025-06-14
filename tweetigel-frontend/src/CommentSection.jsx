import {useContext, useState} from "react";
import {API} from "./Context.js";

function CommentSection({commentsCount, postId}){
    const api = useContext(API)
    const [comments, setComments] = useState([]);

    function getComments(){
        event.preventDefault();
        fetch(api+"/post/" + postId + "/comments", {
            method: 'GET'
        }).then(response => {
                if (!response.ok) {
                    alert("Unable to get comments: " + response.statusText)
                    return []
                } else {
                    return response.json()
                }
            }).then(parsedResponse => {
                setComments(parsedResponse);
        })
    }

    function addComment(){


    }

    return <>
        <details>
            <summary role="button" className="outline" onClick={getComments}>
                {commentsCount} Comment{commentsCount>1?"s":""}
            </summary>
            <button className="pico-background-jade-350" onClick={addComment}>Leave Comment</button>
            {comments.length === 0?
                <p>No comments yet.</p>
                : comments.map(comment => (
                    <p key={comment.id}>
                        {comment.author}@{comment.timestamp}: {comment.content}
                    </p>
                ))
            }
        </details>
    </>
}

export default CommentSection