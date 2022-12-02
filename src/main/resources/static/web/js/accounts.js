const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            data: [],
            client: {},
            accounts:[],
        }
    },
    created() {
        this.getData();
    },
    mounted() {

    },
    methods: {
        getData() {
            axios.get("http://localhost:8080/api/clients/1")
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    this.client = data;
                    this.accounts = this.client.accounts;
                    console.log(this.accounts);
                })
                .catch(error => console.error(error))
        },
    },
    computed: {

    }
})
app.mount('#app')