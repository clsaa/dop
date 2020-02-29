import React, {Component} from 'react';
import './Styles.scss'


export default class Iframe extends Component{
    componentDidMount(){
        setTimeout(()=>{
            document.getElementById("iframe").src = this.props.src
        },1000)
    }

    onLoad(){
        this.props.onLoad(false)
    }
    render(){
        return (
            <div className="wrapper">
                <iframe
                    id = "iframe"
                    title= "iframe"
                    frameBorder= "0"
                    scrolling = "yes"
                    className="iframe"
                />
            </div>
        )
    }
}
