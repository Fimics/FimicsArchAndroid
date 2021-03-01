import React from 'react';
import './Index.less';
import {Table, Input, Button} from 'antd';
import {SearchOutlined} from '@ant-design/icons';
import Highlighter from 'react-highlight-words';
import api from '../../service'
import NavigationUtil, {RouteConfig} from '../../component/Navigator'

const PAGE_SIZE = 10;
export default class Index extends React.Component {
    // "id": 4,
    // "namespace": "hilog",
    // "version": "202608080004",
    // "createTime": "2026-08-22 16:20:30",
    // "originalUrl": "https://o.devio.org/files/hilog_1.0.0_20280822162030",
    // "jsonUrl": "https://o.devio.org/files/hilog_1.0.0_20280822162030.json",
    // "content": null
    state = {
        list: [],
        total: 0,
        loading: false,
        searchText: '',
        searchedColumn: ''
    };
    getColumnSearchProps = dataIndex => ({
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    ref={node => this.searchInput = node}
                    placeholder={`搜索 ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{width: 188, marginBottom: 8, display: 'block'}}
                />
                <Button
                    type='primary'
                    onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    icon={<SearchOutlined/>}
                    size='small'
                    style={{width: 90, marginRight: 8}}
                >
                    搜索
                </Button>
                <Button
                    size='small'
                    style={{width: 90}}
                    onClick={() => this.handleReset(clearFilters)}
                >
                    重置
                </Button>
            </div>
        ),
        filterIcon: filtered => (
            <SearchOutlined style={{color: filtered ? '#1990ff' : undefined}}/>
        ),
        onFilter: (value, record) =>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase())
        , onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => this.searchInput.select());
            }
        },
        render: text => (
            (this.state.searchedColumn === dataIndex) ?
                <Highlighter
                    highlightStyle={{backgroundColor: '#ffc069', padding: 0}}
                    searchWords={[this.state.searchText]}
                    autoEscape
                    textToHighlight={text.toString()}
                /> : text && text.toString()
        )
    });
    columns = [
        {
            title: 'id',
            dataIndex: 'id',
            key: 'id',
            width: '10%',
            fixed: 'left',
            ...this.getColumnSearchProps('id')
        },
        {
            title: 'namespace',
            dataIndex: 'namespace',
            key: 'namespace',
            width: "20%",
            ...this.getColumnSearchProps('namespace')
        },
        {
            title: 'version',
            dataIndex: 'version',
            key: 'version',
            width: '20%',
            ...this.getColumnSearchProps('version')
        },
        {
            title: '发布时间',
            dataIndex: 'createTime',
            key: 'createTime',
            width: '25%',
            ...this.getColumnSearchProps('createTime')
        },
        {
            title: <div className='config-operation'>操作</div>,
            dataIndex: 'operation',
            width: '30%',
            render: (text, record) => {
                return <div className='config-operation' onClick={() => this.handleClick(record)}>
                    <a>编辑</a>
                </div>
            }
        }

    ];

    componentDidMount() {
        this.setState({
            loading: true
        });
        this.loadData(1);
    }

    handleClick(item) {
        NavigationUtil.goto(RouteConfig.configAdd, this.props.history)({state: {item}})
    }

    handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        this.setState({
            searchText: selectedKeys[0],
            searchedColumn: dataIndex
        })
    };
    handleReset = clearFilters => {
        clearFilters();
        this.setState({
            searchText: ''
        })
    };


    loadData(pageIndex) {
        this.pageIndex = pageIndex;
        api.getConfig({pageIndex, pageSize: PAGE_SIZE})
            .then(response => response.json())
            .then(result => {
                const {data: {list, total} = {}} = result;
                this.setState({
                    list: list,
                    total,
                    loading: false
                })
            })
            .catch(e => {
                console.log(e);
                this.setState({
                    loading: false,
                })
            });
    }

    render() {
        const {list, total, loading} = this.state;

        return <Table
            columns={this.columns}
            rowKey={item => item.id}
            dataSource={list}
            scroll={{x: 1000, y: 600}}
            loading={loading}
            pagination={
                {
                    total,
                    pageSize: PAGE_SIZE,
                    onChange: (page, pageSize) => {
                        console.log(page, pageSize);
                        this.loadData(page);
                    }
                }
            }
        />
    }

}
