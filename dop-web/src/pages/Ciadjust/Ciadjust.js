import React, { Component } from 'react';
import Axios from 'axios';
import API from '../API';
import { Input } from '@icedesign/base';
import { Link } from 'react-router-dom';
import { Button } from '@icedesign/base';
import TopBar from '../../components/TopBar';
import './Ciadjust.scss';

export default class Ciadjust extends Component {
  static displayName = 'Ciadjust';

  render() {
    return (
    <div className="buildbody">
      <div className="tobarcontent">
        <Input
          size="large"
          placeholder="请输入项目git地址"
          style={{ width: '240px'}}
        />
         <Button type="primary" class="tobarButton">提交</Button><br /><br/>
      </div>

       <div className="tobarcontent">
           <Button type="primary">构建结果预测</Button><br /><br/>
       </div>
    </div>
    );
  }
}
