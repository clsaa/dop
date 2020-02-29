import React from 'react';

import './SSHREADME.css'

export default class SSHREADME extends React.Component{


    constructor(props){
        super(props);
        this.state={

        }
    }

    render(){
        return (
            <div className="ssh-readme-container">
                <h1>SSH</h1>
                <h2>SSH Keys</h2>
                <p>SSH key 可以让你在你的电脑和Code服务器之间建立安全的加密连接。 先执行以下语句来判断是否已经存在本地公钥：</p>
                <pre>
                    <code>cat ~/.ssh/id_rsa.pub</code>
                </pre>
                <p>如果你看到一长串以 <code>ssh-rsa</code>或 <code>ssh-dsa</code>开头的字符串, 你可以跳过 <code>ssh-keygen</code>的步骤。</p>
                <p>提示: 最好的情况是一个密码对应一个ssh key，但是那不是必须的。你完全可以跳过创建密码这个步骤。请记住设置的密码并不能被修改或获取。</p>
                <p>你可以按如下命令来生成ssh key：</p>
                <pre><code>ssh-keygen -t rsa -C <span>""</span></code></pre>
                <p>这个指令会要求你提供一个位置和文件名去存放键值对和密码，你可以点击Enter键去使用默认值。</p>
                <p>用以下命令获取你生成的公钥：</p>
                <pre><code>cat ~/.ssh/id_rsa.pub</code></pre>
                <p>复制这个公钥放到你的个人设置中的SSH/My SSH Keys下，请完整拷贝从<code>ssh-</code>开始直到你的用户名和主机名为止的内容。</p>
                <p>如果打算拷贝你的公钥到你的粘贴板下，请参考你的操作系统使用以下的命令：</p>
                <p><strong>Windows:</strong></p>
                <pre><code>clip &lt; ~/.ssh/id_rsa.pub</code></pre>
                <p><strong>Mac:</strong></p>
                <pre><code>pbcopy &lt; ~/.ssh/id_rsa.pub</code></pre>
                <p><strong>GNU/Linux (requires xclip):</strong></p>
                <pre><code>xclip -sel clip &lt; ~/.ssh/id_rsa.pub</code></pre>
            </div>
        )
    }

}
