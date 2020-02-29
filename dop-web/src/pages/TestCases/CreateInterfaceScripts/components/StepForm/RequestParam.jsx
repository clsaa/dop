import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid, Icon,
} from '@icedesign/base';

import {  FormBinder, FormError } from '@icedesign/form-binder';
import Select, {Option} from "@icedesign/base/lib/select";
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;

const styles = {
    formItem: {
        marginBottom: '20px',
        display: 'flex',
        alignItems: 'center',
    },
    formItemLabel: {
        width: '70px',
        marginRight: '10px',
        display: 'inline-block',
        textAlign: 'right',
    },
    formItemError: {
        marginLeft: '10px',
    },
    preview: {
        border: '1px solid #eee',
        marginTop: 20,
        padding: 10
    }
};

class RequestParam extends Component{

    render() {
        return (
            <div>
                {this.props.requestParams.map((requestParam, index) => {
                    return (
                        <Row key={index} >

                            <Col>
                                <FormBinder required message={this.props.intl.messages['test.requestParam.type']} name={`requestParams[${index}].paramClass`}>
                                    <Select style={{width: '100%'}}
                                            placeholder={this.props.intl.messages['test.requestParam.place']}
                                    >
                                        <Option value="GET_PARAM">GET PARAM</Option>
                                        <Option value="PATH_PARAM">PATH PARAM</Option>
                                        <Option value="FILE_PARAM">FILE</Option>
                                    </Select>
                                </FormBinder>
                            </Col>
                            &nbsp;

                            <Col>
                                <FormBinder required message={this.props.intl.messages['test.requestParam.nameWarn']} name={`requestParams[${index}].name`} >
                                    <Input placeholder="name" style={{width: '100%'}} />
                                </FormBinder>
                                <FormError name={`requestParams[${index}].name`} />
                            </Col>
                            &nbsp;

                            <Col>
                                <FormBinder name={`requestParams[${index}].value`} required message={this.props.intl.messages['test.requestParam.valueWarn']} >
                                    <Input placeholder="value" style={{width: '100%'}}/>
                                </FormBinder>
                                <FormError name={`requestParams[${index}].value`} />
                            </Col>
                            <Col>
                                <Button type="secondary" onClick={this.props.removeItem.bind(this, index)}>
                                    <Icon type="ashbin" size='xxxl'/>
                                </Button>
                            </Col>
                        </Row>
                    );
                })}
                <div style={styles.buttons}>
                    <Button type="secondary" onClick={this.props.addItem}>
                        <Icon type="add" size='xxl'/>
                    </Button>
                </div>
            </div>
        );
    }
}

export default injectIntl(RequestParam);
