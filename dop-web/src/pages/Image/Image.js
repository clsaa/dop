import React, { Component } from 'react';
import "./Style.scss"
import NamespacePagination from './NamespaceManagement/NamespacePagination';
import {FormattedMessage} from 'react-intl';

export default class Image extends Component {
    render() {
        return (
            <div>
                <NamespacePagination/>
            </div>
        )
    };

}
