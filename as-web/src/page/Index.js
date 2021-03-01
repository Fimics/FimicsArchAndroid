import React from 'react';
import './Index.less'
import api from '../service'
import {Button} from 'antd'

export default class Index extends React.Component {
    state = {
        result: {}
    };
    fire = () => {
        api.userList({pageIndex: 1, pageSize: 10})
            .then(res => res.json())
            .then(result => {
                this.setState({
                    result
                });
            }).catch(e => {
            console.log(e);
        })
    };

    render() {
        const {result} = this.state;
        return <div className='home'>
            Home
            <Button onClick={this.fire}>test API</Button>
            <div>
                Result:{JSON.stringify(result)}
            </div>
        </div>
    }

}
