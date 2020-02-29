import React, {PureComponent} from 'react';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';
import logo from './images/logo.png'

export default class Logo extends PureComponent {
    render() {
        return (
            <Link to="/" style={{...styles.logoStyle, ...this.props.style}}>
                {(() => {
                    if (this.props.img) {
                        return (
                            <span style={{...styles.imgStyle}}>
                                <img src={logo} alt="" width="100%"/>
                            </span>
                        )
                    }
                })()}
                <FormattedMessage
                    id="base.title"
                    defaultMessage="DevOps Platform"
                />
            </Link>
        );
    }
}

const styles = {
    logoStyle: {
        display: 'block',
        maxWidth: '360px',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
        fontSize: '20px',
        color: '#fff',
        fontWeight: 'bold',
        textDecoration: 'none',
    },
    imgStyle: {
        display: 'inline-block',
        width: '80px',
        height: '60px',
        marginRight: '20px'
    }
};
