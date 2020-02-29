import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import './NotPermission.scss';

export default class NotPermission extends Component {
    static displayName = 'NotPermission';

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div className="not-permission" style={styles.notPermission}>
                <IceContainer>
                    <div style={styles.content} className="exception-content">
                        <img
                            src={require('./images/notpermission.png')}
                            style={styles.imgException}
                            className="imgException"
                            alt="prmission"
                        />
                        <div style={styles.prompt}>
                            <h3 style={styles.title} className="title">
                                Sorry, you are not running the pipeline yet～
                            </h3>
                            <p style={styles.description} className="description">
                                Sorry, you haven't run the pipeline yet, click run～
                            </p>
                        </div>
                    </div>
                </IceContainer>
            </div>
        );
    }
}

const styles = {
    content: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        color: '#333',
    },
    description: {
        color: '#666',
    },
};
