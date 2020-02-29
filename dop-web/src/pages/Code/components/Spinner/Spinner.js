import React from 'react';

import './Spinner.css'

export default class Spinner extends React.Component{

    render(){
        return (
            <div className="spinner">
                <div className="spinner-container spinner-container1">
                    <div className="spinner-circle1"/>
                    <div className="spinner-circle2"/>
                    <div className="spinner-circle3"/>
                    <div className="spinner-circle4"/>
                </div>
                <div className="spinner-container spinner-container2">
                    <div className="spinner-circle1"/>
                    <div className="spinner-circle2"/>
                    <div className="spinner-circle3"/>
                    <div className="spinner-circle4"/>
                </div>
                <div className="spinner-container spinner-container3">
                    <div className="spinner-circle1"/>
                    <div className="spinner-circle2"/>
                    <div className="spinner-circle3"/>
                    <div className="spinner-circle4"/>
                </div>
            </div>
        )
    }

}
