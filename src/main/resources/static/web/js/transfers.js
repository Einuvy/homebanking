const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            data: [],
            client: {},
            accounts: [],
            accountOrigin: "Origin Account...",
            amount: [],
            accountDestinatary: "Destinatary Account...",
            description:"",
            sendOtherAccountOrigin:"Origin Account...",
            sendOtherAccount:"",
            sendOtherAmount:"",
            sendOtherDescription:"",
            recipientClient:{},
        }
    },
    created() {
        this.getData();
    },
    mounted() {
    },
    methods: {
        getData() {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    let USDollar = new Intl.NumberFormat("en-US",
                        {
                            style: "currency",
                            currency: "USD",
                        });
                    this.client = data;
                    this.accounts = this.client.accounts;
                    this.accounts.map(account => account.balance = USDollar.format(account.balance))
                    console.log(this.accounts)
                })
                .catch(error => console.error(error))
        },
        transfer() {
            if((this.accountOrigin === "Origin Account...") || (this.accountDestinatary === "Destinatary Account...") || (this.amount === [])){
                Swal.fire({
                    title: 'Error',
                    text: `You need complete all fields`,
                    icon: 'error',
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                })
            }else{
                Swal.fire({
                    didOpen: (event) => {
                        document.querySelector('.swal2-confirm').addEventListener("click", () => {
                            this.sendTransfer()
                        })
                    },
                    title: 'Are you sure?',
                    text: `Dou you send $${this.amount} to account: ${this.accountDestinatary}
                    Description:${this.description}
                    `,
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, send!',
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                }) 
            }
            
        },
        sendTransfer(){
            axios.post('/api/transactions',
            "amount="+this.amount+
            "&description="+this.description+
            "&numberOrigin="+this.accountOrigin+
            "&numberRecipients="+this.accountDestinatary)
            .then(response => {
                Swal.fire({
                    title:'Transfer send!',
                    icon: 'success',
                    text: `${response.data}`,
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                })
            })
            .catch(error => {
                Swal.fire({
                    title:'Transfer error!',
                    icon: 'error',
                    text: `${error.response.data}`,
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                })
                console.log(error);
            })
        },
        otherTransfer(){
            if((this.sendOtherAccountOrigin === "Origin Account...") || (this.sendOtherAccount === "")|| (this.sendOtherAmount === [])){
                Swal.fire({
                    title: 'Error',
                    text: `You need complete all fields`,
                    icon: 'error',
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                })
            }else{
                this.sendOtherAccount = this.sendOtherAccount.toUpperCase()
                axios.get("/api/clients/account/send/"+this.sendOtherAccount)
                .then(response => {
                    this.recipientClient = response.data;
                    
                    Swal.fire({
                        didOpen: (event) => {
                            
                            document.querySelector('.swal2-confirm').addEventListener("click", () => {
                                this.sendOtherTransfer()
                            })
                        },
                        title: 'Are you sure?',
                        text: `Dou you send $${this.sendOtherAmount} 
                        to client: ${this.recipientClient.lastName}, ${this.recipientClient.firstName} 
                        account: ${this.sendOtherAccount}
                         with description: ${this.sendOtherDescription}
                        `,
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Yes, send!',
                        customClass: {
                            popup: "text-white borderRadius-15",
                        }
                    })  
                })
                .catch(error => {
                    Swal.fire({
                        title:'Transfer error!',
                        icon: 'error',
                        text: "Account not found",
                        customClass: {
                            popup: "text-white borderRadius-15",
                        }
                    })
                })
            }
            
        },
        sendOtherTransfer(){
            this.sendOtherAccount = this.sendOtherAccount.toUpperCase()
            axios.post('/api/transactions',
            "amount="+this.sendOtherAmount+
            "&description="+this.sendOtherDescription+
            "&numberOrigin="+this.sendOtherAccountOrigin+
            "&numberRecipients="+this.sendOtherAccount)
            .then(response => {
                Swal.fire({
                    title:'Transfer send!',
                    icon: 'success',
                    text: `${response.data}`,
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                })
            })
            .catch(error => {
                Swal.fire({
                    title:'Transfer error!',
                    icon: 'error',
                    text: `${error.response.data}`,
                    customClass: {
                        popup: "text-white borderRadius-15",
                    }
                })
            })
        }
    },
    computed: {

    }
}).mount('#app')